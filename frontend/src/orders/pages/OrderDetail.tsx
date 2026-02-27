import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { cancelOrder, getOrderById, type OrderResponse } from "../services/ordersData";
import OrderStatusBadge from "../components/OrderStatusBadge";
import toast from "react-hot-toast";
import { createPayment, verifyPayment } from "../services/paymentApi";
import { loadRazorpayScript } from "../../shared/utils/loadRazorpay";

const OrderDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const orderId = Number(id);

  const [order, setOrder] = useState<OrderResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [cancelLoading, setCancelLoading] = useState(false);
  const [paymentLoading, setPaymentLoading] = useState(false);

  useEffect(() => {
    const fetchOrder = async () => {
      try {
        const data = await getOrderById(orderId);
        setOrder(data);
      } finally {
        setLoading(false);
      }
    };

    if (!isNaN(orderId)) fetchOrder();
    else setLoading(false);
  }, [orderId]);

  if (loading) {
    return (
      <div className="max-w-5xl mx-auto px-4 py-20 text-center text-gray-500">
        Loading order details...
      </div>
    );
  }

  if (!order) {
    return (
      <div className="max-w-5xl mx-auto px-4 py-20 text-center">
        <h2 className="text-lg font-semibold">Order not found</h2>
        <Link to="/orders" className="text-green-600 hover:underline mt-3 block">
          Back to Orders
        </Link>
      </div>
    );
  }

  const formattedDate = new Date(order.createdAt).toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });

  const canCancel =
  order.orderStatus === "CREATED";

  const canPay =
    order.paymentStatus !== "SUCCESS" &&
    order.orderStatus !== "CANCELLED";

  const handleCancelOrder = async () => {
    if (!canCancel) return;

    const confirmed = window.confirm(
      "Are you sure you want to cancel this order?"
    );

    if (!confirmed) return;

    try {
      setCancelLoading(true);

      await cancelOrder(order.orderId);

      setOrder({
        ...order,
        orderStatus: "CANCELLED",
      });

      toast.success("Order cancelled successfully");
    } catch (error: any) {
      if (error.response?.status === 409) {
        toast.error(error.response.data.message ?? "Order cannot be cancelled");
      } else {
        toast.error("Something went wrong");
      }
    } finally {
      setCancelLoading(false);
    }
  };

  const handlePayment = async () => {
    if (!order) return;

    setPaymentLoading(true);

    const isLoaded = await loadRazorpayScript();
    if (!isLoaded) {
      toast.error("Failed to load Razorpay SDK");
      setPaymentLoading(false);
      return;
    }

    try {
      const razorpayOrderId = await createPayment(order.orderId);

      const options = {
        key: import.meta.env.VITE_RAZORPAY_KEY,
        amount: order.totalAmount * 100,
        currency: "INR",
        name: "BuildWithRani",
        description: `Order #${order.orderId}`,
        order_id: razorpayOrderId,
        handler: async function (response: any) {
          await verifyPayment({
            orderId: order.orderId,
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            signature: response.razorpay_signature,
          });

          toast.success("Payment successful!");
          window.location.reload();
        },
        theme: {
          color: "#111827",
        },
      };

      const rzp = new (window as any).Razorpay(options);
      rzp.open();

    } catch (error) {
      toast.error("Payment failed. Please try again.");
    } finally {
      setPaymentLoading(false);
    }
  };

  return (
    <div className="max-w-5xl mx-auto px-4 py-22 space-y-8">

      {/* HEADER */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Order #{order.orderId}
          </h1>
          <p className="text-sm text-gray-500 mt-1">
            Placed on {formattedDate}
          </p>
        </div>

        <OrderStatusBadge status={order.orderStatus} />
      </div>

      {/* ITEMS */}
      <div className="bg-white border border-gray-200 rounded-xl divide-y">
        {order.items.map((item) => (
          <div
            key={item.productId}
            className="flex items-center gap-4 p-5"
          >
            <div className="w-14 h-14 rounded-lg bg-green-50 text-green-700 font-bold flex items-center justify-center text-lg shrink-0">
              {item.productName.charAt(0)}
            </div>

            <div className="flex-1">
              <p className="font-medium text-gray-900">
                {item.productName}
              </p>
              <p className="text-sm text-gray-500 mt-1">
                Quantity: {item.quantity}
              </p>
              <p className="text-xs text-gray-400 mt-0.5">
                ₹{item.priceAtPurchase} × {item.quantity}
              </p>
            </div>

            <div className="text-right">
              <p className="font-semibold text-gray-900">
                ₹{item.totalPrice}
              </p>
            </div>
          </div>
        ))}
      </div>

      {/* SUMMARY */}
      <div className="bg-white border border-gray-200 rounded-xl p-5 space-y-3 max-w-md ml-auto">
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Items</span>
          <span>{order.items.length}</span>
        </div>

        <div className="flex justify-between text-base font-semibold">
          <span>Total Amount</span>
          <span>₹{order.totalAmount}</span>
        </div>
      </div>

      {/* ACTIONS */}
      <div className="flex justify-end gap-3">

        {canPay && (
          <button
            onClick={handlePayment}
            disabled={paymentLoading}
            className={`px-4 py-2 rounded-md text-sm font-medium bg-black text-white hover:bg-gray-800 ${
              paymentLoading ? "opacity-70 cursor-wait" : ""
            }`}
          >
            {paymentLoading ? "Processing..." : "Pay Now"}
          </button>
        )}

        <button
          disabled={!canCancel || cancelLoading}
          onClick={handleCancelOrder}
          className={`px-4 py-2 rounded-md text-sm font-medium ${
            canCancel
              ? "bg-red-600 text-white hover:bg-red-700"
              : "bg-gray-200 text-gray-500 cursor-not-allowed"
          } ${cancelLoading ? "opacity-70 cursor-wait" : ""}`}
        >
          {cancelLoading ? "Cancelling..." : "Cancel Order"}
        </button>
      </div>

      {!canCancel && (
        <p className="text-xs text-gray-400 text-right mt-1">
          This order can no longer be cancelled.
        </p>
      )}

      <Link
        to="/orders"
        className="inline-block text-sm text-gray-500 hover:text-gray-700 hover:underline"
      >
        ← Back to Orders
      </Link>
    </div>
  );
};

export default OrderDetail;
import axios from "../../api/axiosInstance";

export const createPayment = async (orderId: number) => {
  const response = await axios.post("/payments/create", {
    orderId,
  });
  return response.data.data; // razorpayOrderId
};

export const verifyPayment = async (payload: {
  orderId: number;
  razorpayOrderId: string;
  razorpayPaymentId: string;
  signature: string;
}) => {
  await axios.post("/payments/verify", payload);
};
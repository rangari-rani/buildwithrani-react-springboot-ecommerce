export interface Product {
  id: number;

  name: string;
  description: string;

  price: number;
  discountPercentage?: number;

  imageUrl?: string;

  featured: boolean;
  status: "ACTIVE" | "INACTIVE";

  averageRating?: number;

  createdAt?: string;
}

export interface Product {
  id: number;
  name: string;
  price: number;
  image: string;
  isFeatured: boolean;
  createdAt?: string; // ISO string
}

export const products: Product[] = [
  {
    id: 1,
    name: "Herbal Face Wash with Neem",
    price: 299,
    image: "https://th.bing.com/th/id/R.45ea57f6fdcba0ecab919bbfbdb985df?rik=4c5rAfLa2N7o%2fg&riu=http%3a%2f%2fwww.pngall.com%2fwp-content%2fuploads%2f2017%2f03%2fOlive-Oil-Free-PNG-Image.png&ehk=VKnCaYtuIrj63CcA6xKcHMlhzou1J2ao00fvIHmZvPM%3d&risl=&pid=ImgRaw&r=0",
    isFeatured: true,
   
  },
  {
    id: 2,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: true,
   
  },
  {
    id: 3,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: true,
   
  },
  {
    id: 4,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: true,
    
  },
  {
    id: 5,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: true,

  },
  {
    id: 6,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: false,
    createdAt: "2026-01-06T09:15:00",
  },
  {
    id: 7,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: false,
    createdAt: "2026-01-06T09:15:00",
  },
  {
    id: 8,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: false,
    createdAt: "2026-01-06T09:15:00",
  },
  {
    id: 9,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: false,
    createdAt: "2026-01-06T09:15:00",
  },
  {
    id: 10,
    name: "Organic Aloe Vera Gel",
    price: 249,
    image: "...",
    isFeatured: false,
    createdAt: "2026-01-06T09:15:00",
  },
];


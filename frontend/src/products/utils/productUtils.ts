export const isNewArrival = (
  createdAt: string,
  days: number = 7
): boolean => {
  const createdDate = new Date(createdAt);
  const now = new Date();

  const diffInMs = now.getTime() - createdDate.getTime();
  const diffInDays = diffInMs / (1000 * 60 * 60 * 24);

  return diffInDays <= days;
};

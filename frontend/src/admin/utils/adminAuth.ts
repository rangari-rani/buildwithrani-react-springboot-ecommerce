export const isAdminLoggedIn = () =>
  localStorage.getItem("isAdmin") === "true";

export const adminLogout = () => {
  localStorage.removeItem("isAdmin");
};

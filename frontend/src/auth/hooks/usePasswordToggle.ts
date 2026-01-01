import { useState } from "react";

export function usePasswordToggle() {
  const [visible, setVisible] = useState(false);

  const toggle = () => setVisible((v) => !v);
  const type = visible ? "text" : "password";

  return { type, visible, toggle };
}

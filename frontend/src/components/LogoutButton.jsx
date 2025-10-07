import React from "react";

const LogoutButton = () => {
  const logout = () => {
    // Clear tokens if any and redirect
    window.location.href = "/";
  };

  return <button onClick={logout}>Logout</button>;
};

export default LogoutButton;

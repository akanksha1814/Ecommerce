import React from "react";

const LoginButton = () => {
  const login = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  return <button onClick={login}>Login with Google</button>;
};

export default LoginButton;

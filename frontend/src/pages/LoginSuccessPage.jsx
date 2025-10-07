import React, { useEffect, useState } from "react";
import axios from "axios";
import UserProfile from "../components/UserProfile";
import LogoutButton from "../components/LogoutButton";

const LoginSuccessPage = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    axios.get("http://localhost:8080/auth/user", { withCredentials: true }).then((res) => {
      setUser(res.data);
      localStorage.setItem("auth", "true");
    });
  }, []);

  return (
    <div>
      <h2>Login Successful</h2>
      {user && <UserProfile user={user} />}
      <LogoutButton />
    </div>
  );
};

export default LoginSuccessPage;

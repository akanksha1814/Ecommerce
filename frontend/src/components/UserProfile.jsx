import React from "react";

const UserProfile = ({ user }) => {
  return (
    <div>
      <img src={user.picture} alt={user.name} width="50" />
      <h2>{user.name}</h2>
      <p>{user.email}</p>
    </div>
  );
};

export default UserProfile;

import { AnyRecord } from "dns";
import { db } from "./db-connection";
import { User } from "./user"
import { collection, getDocs, doc } from "firebase/firestore";

export const getAllUsers = async (): Promise<User[]> => {
  try {
    const snapshot = await db.collection("users").get();

    const users: User[] = snapshot.docs.map((doc) => {
      const userData = doc.data();
      return {
        userId: doc.id,
        userEmail: userData.email,
        userCar: userData.car || {},
      };
    });

    return users;

  } catch (error) {
    console.error("Error fetching users:", error);
    return [];
  }
};

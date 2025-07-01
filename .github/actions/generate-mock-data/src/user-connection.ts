import { AnyRecord } from "dns";
import { db } from "./db-connection";
import { User } from "./user"
import { collection, getDocs, doc } from "firebase/firestore";

export const getAllUsers = async (): Promise<User[]> => {
  try {
    const querySnapshot = await getDocs(collection(db, "users"));
    const users: User[] = querySnapshot.docs.map((doc) => {
      const userData = doc.data();
      return {
        userId: doc.id as string,
        userEmail: userData.email as string,
        userCar: userData.car || {},
      };
    });

    return users;

  } catch (error) {
    console.error("Error fetching users:", error);
    return [];
  }
};

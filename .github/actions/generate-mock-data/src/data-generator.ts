import { db } from "./db-connection";
import { User } from "./user";
import { collection, addDoc, doc } from "firebase/firestore";


// RENAULT MEGANE 4 1.5dci 2017
const getFuelConsumption = (): string => {
    const minFuel = 4.0; // Minimum realistic fuel usage per 100km
    const maxFuel = 6.0; // Maximum realistic fuel usage per 100km
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(2)}L`;
};

const getDistance = (): string => {
    const minDistance = 15; // Shortest trip distance
    const maxDistance = 500; // Longest realistic trip distance
    return `${(Math.random() * (maxDistance - minDistance) + minDistance).toFixed(1)} km`;
};

export const saveFuelReport = async (user: User) => {
    const report = {
      fuelUsed: getFuelConsumption(),
      distance: getDistance(),
      date: new Date().toISOString(),
    };

  try {
    const userRef = doc(db, "users", user.userId.toString());
    await addDoc(collection(userRef, "reports"), report);
    //await addDoc(collection(db, "car_reports"), report);
    console.log("New report uploaded:", report);
  } catch (error) {
    console.error("Could not upload report:", error);
  }
};

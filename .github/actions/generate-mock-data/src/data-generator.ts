import { db } from "./db-connection";
import { User } from "./user";
import { collection, addDoc, doc, getDocs } from "firebase/firestore";


// DACIA DUSTER 1.5 dci
const setDaciaDusterConsumption = (): string => {
    const minFuel = 5.0;
    const maxFuel = 8.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
}

// DACIA SANDERO 1.2 fsi
const setDaciaSanderoConsumption = (): string => {
  const minFuel = 3.0;
  const maxFuel = 6.0;
  return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
}

// SKODA OCTAVIA 2.0 tdi
const setSkodaOctaviaConsumption = (): string => {
  const minFuel = 5.0;
  const maxFuel = 9.0;
  return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
}

// SKODA FABIA 1.4 fsi
const setSkodaFabiaConsumption = (): string => {
  const minFuel = 3.0;
  const maxFuel = 7.0;
  return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
}

// RENAULT MEGANE 4 1.5 dci 2017
const setRenaultMeganeConsumption = (): string => {
    const minFuel = 4.0;
    const maxFuel = 7.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
};

const getFuelConsumption = (type: String): String => {
  var report = ""
  switch(type) {
    case "Megane 4":
      report = setRenaultMeganeConsumption()
      break;
    case "Sandero":
      report = setDaciaSanderoConsumption()
      break;
    case "Duster":
      report = setDaciaDusterConsumption()
      break;
    case "Octavia":
      report = setSkodaOctaviaConsumption()
      break;
    case "Fabia":
      report = setSkodaFabiaConsumption()
      break;
  }
  return report
};

const getDistance = (): string => {
    const minDistance = 15; // Shortest trip distance
    const maxDistance = 500; // Longest trip distance
    return `${(Math.random() * (maxDistance - minDistance) + minDistance).toFixed(1)} km`;
};

const getRandomLitersUsed = (): string => {
  const total = Math.random() * 40 + 5; // 5L to 45L
  return `${total.toFixed(1)} L`;
};

export const getUserCars = async (userId: String) => {
  const carsRef = db.collection(`users/${userId}/cars`);
  const snapshot = await carsRef.get();
  if (snapshot.empty) return null;

  // Just use the first car document for this user
  const carDoc = snapshot.docs[0];
  return carDoc.data();
};

export const saveFuelReport = async (user: User) => {

const car = await getUserCars(user.userId);
if (!car || !car.type) {
  console.warn(`User ${user.userId} has no valid car data.`);
  return;
}

const consumption = getFuelConsumption(car.type)
  const report = {
    fuelUsed: getRandomLitersUsed(),
    mediumConsumptionLevel: consumption,
    distance: getDistance(),
    date: new Date().toISOString(),
  };

  try {
    const reportsRef = db
      .collection("users")
      .doc(user.userId.toString())
      .collection("reports");

    await reportsRef.add(report);

    console.log("New report uploaded:", report);
  } catch (error) {
    console.error("Could not upload report:", error);
  }
};
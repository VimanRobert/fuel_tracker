import { db } from "./db-connection";
import { User } from "./user";
import { collection, addDoc, doc } from "firebase/firestore";


// DACIA DUSTER 1.5 dci
const setDaciaDusterConsumption = (): string => {
    const minFuel = 5.0;
    const maxFuel = 8.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(2)}L`;
}

// DACIA SANDERO 1.2 fsi
const setDaciaSanderoConsumption = (): string => {
  const minFuel = 3.0;
  const maxFuel = 6.0;
  return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(2)}L`;
}

// SKODA OCTAVIA 2.0 tdi
const setSkodaOctaviaConsumption = (): string => {
  const minFuel = 5.0;
  const maxFuel = 9.0;
  return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(2)}L`;
}

// SKODA FABIA 1.4 fsi
const setSkodaFabiaConsumption = (): string => {
  const minFuel = 3.0;
  const maxFuel = 7.0;
  return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(2)}L`;
}

// RENAULT MEGANE 4 1.5 dci 2017
const setRenaultMeganeConsumption = (): string => {
    const minFuel = 4.0;
    const maxFuel = 7.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(2)}L`;
};

const getFuelConsumption = (model: String): String => {
  var report = ""
  switch(model) {
    case "Megane 4": {
      report = setRenaultMeganeConsumption()
    }
    case "Sandero": {
      report = setDaciaSanderoConsumption()
    }
    case "Duster": {
      report = setDaciaDusterConsumption()
    }
    case "Octavia": {
      report = setSkodaOctaviaConsumption()
    }
    case "Fabia": {
      report = setSkodaFabiaConsumption()
    }
  }
  return report
};

const getDistance = (): string => {
    const minDistance = 15; // Shortest trip distance
    const maxDistance = 500; // Longest trip distance
    return `${(Math.random() * (maxDistance - minDistance) + minDistance).toFixed(1)} km`;
};

export const saveFuelReport = async (user: User) => {
    const report = {
      fuelUsed: getFuelConsumption(user.car!!.brand),
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

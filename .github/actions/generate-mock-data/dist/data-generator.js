"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.saveFuelReport = exports.getUserCars = void 0;
const db_connection_1 = require("./db-connection");
// DACIA DUSTER 1.5 dci
const setDaciaDusterConsumption = () => {
    const minFuel = 5.0;
    const maxFuel = 8.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
};
// DACIA SANDERO 1.2 fsi
const setDaciaSanderoConsumption = () => {
    const minFuel = 3.0;
    const maxFuel = 6.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
};
// SKODA OCTAVIA 2.0 tdi
const setSkodaOctaviaConsumption = () => {
    const minFuel = 5.0;
    const maxFuel = 9.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
};
// SKODA FABIA 1.4 fsi
const setSkodaFabiaConsumption = () => {
    const minFuel = 3.0;
    const maxFuel = 7.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
};
// RENAULT MEGANE 4 1.5 dci 2017
const setRenaultMeganeConsumption = () => {
    const minFuel = 4.0;
    const maxFuel = 7.0;
    return `${(Math.random() * (maxFuel - minFuel) + minFuel).toFixed(1)}`;
};
const getFuelConsumption = (type) => {
    var report = "";
    switch (type) {
        case "Megane 4":
            report = setRenaultMeganeConsumption();
            break;
        case "Sandero":
            report = setDaciaSanderoConsumption();
            break;
        case "Duster":
            report = setDaciaDusterConsumption();
            break;
        case "Octavia":
            report = setSkodaOctaviaConsumption();
            break;
        case "Fabia":
            report = setSkodaFabiaConsumption();
            break;
    }
    return report;
};
const getDistance = () => {
    const minDistance = 15; // Shortest trip distance
    const maxDistance = 500; // Longest trip distance
    return `${(Math.random() * (maxDistance - minDistance) + minDistance).toFixed(1)} km`;
};
const getRandomLitersUsed = () => {
    const total = Math.random() * 40 + 5; // 5L to 45L
    return `${total.toFixed(1)} L`;
};
const getUserCars = async (userId) => {
    const carsRef = db_connection_1.db.collection(`users/${userId}/cars`);
    const snapshot = await carsRef.get();
    if (snapshot.empty)
        return null;
    // Just use the first car document for this user
    const carDoc = snapshot.docs[0];
    return carDoc.data();
};
exports.getUserCars = getUserCars;
const saveFuelReport = async (user) => {
    const car = await (0, exports.getUserCars)(user.userId);
    if (!car || !car.type) {
        console.warn(`User ${user.userId} has no valid car data.`);
        return;
    }
    const consumption = getFuelConsumption(car.type);
    const report = {
        fuelUsed: getRandomLitersUsed(),
        mediumConsumptionLevel: consumption,
        distance: getDistance(),
        date: new Date().toISOString(),
    };
    try {
        const reportsRef = db_connection_1.db
            .collection("users")
            .doc(user.userId.toString())
            .collection("reports");
        await reportsRef.add(report);
        console.log("New report uploaded:", report);
    }
    catch (error) {
        console.error("Could not upload report:", error);
    }
};
exports.saveFuelReport = saveFuelReport;

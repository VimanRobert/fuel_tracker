"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getAllUsers = void 0;
const db_connection_1 = require("./db-connection");
const getAllUsers = async () => {
    try {
        const snapshot = await db_connection_1.db.collection("users").get();
        const users = snapshot.docs.map((doc) => {
            const userData = doc.data();
            return {
                userId: doc.id,
                userEmail: userData.email,
                userCar: userData.car || {},
            };
        });
        return users;
    }
    catch (error) {
        console.error("Error fetching users:", error);
        return [];
    }
};
exports.getAllUsers = getAllUsers;

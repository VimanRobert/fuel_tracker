"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.db = void 0;
const firebase_admin_1 = __importDefault(require("firebase-admin"));
console.log("CREDENTIALS:");
console.log("PROJECT_ID:", process.env.PROJECT_ID);
console.log("SERVICE_ACCOUNT (exists):", !!process.env.SERVICE_ACCOUNT);
const serviceAccount = JSON.parse(Buffer.from(process.env.SERVICE_ACCOUNT, "base64").toString("utf8"));
if (!firebase_admin_1.default.apps.length) {
    firebase_admin_1.default.initializeApp({
        credential: firebase_admin_1.default.credential.cert(serviceAccount),
        projectId: process.env.PROJECT_ID,
    });
}
exports.db = firebase_admin_1.default.firestore();

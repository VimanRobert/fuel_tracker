import admin from "firebase-admin";

console.log("CREDENTIALS:");
console.log("PROJECT_ID:", process.env.PROJECT_ID);
console.log("SERVICE_ACCOUNT (exists):", !!process.env.SERVICE_ACCOUNT);

const serviceAccount = JSON.parse(
  Buffer.from(process.env.SERVICE_ACCOUNT!, "base64").toString("utf8")
);

if (!admin.apps.length) {
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    projectId: process.env.PROJECT_ID,
  });
}

export const db = admin.firestore();


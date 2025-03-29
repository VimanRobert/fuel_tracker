import "./user"
import { saveFuelReport } from "./data-generator"
import { getAllUsers } from "./user-connection"

const generateReportsForAllUsers = async () => {
    const users = await getAllUsers();
    if (users.length === 0) {
      console.log("No user found.");
      return;
    }
  
    for (const user of users) {
      await saveFuelReport(user);
    }
  
    console.log("Mock data reports generated for all users.");
};
  
generateReportsForAllUsers();

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RaiseTicketForm from "./components/RaiseTicketForm";
import EmployeeConfirmationCard from "./components/EmployeeConfirmationCard";
import ViewDetails from "./components/ViewDetails";
import ProbationEvaluation from './components/ProbationEvaluation';
import ViewFeedback from './components/ViewFeedback';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<EmployeeConfirmationCard />} />
        <Route path="/view-details/:empCode" element={<ViewDetails />} />
        <Route path="/probation-evaluation/:empCode" element={<ProbationEvaluation />} />
        <Route path="/view-feedback/:empCode/:extensionNumber/:threeId/:sixId" element={<ViewFeedback />} />
      </Routes>
    </Router>
  );
}

export default App;

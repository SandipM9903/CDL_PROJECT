import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import RaiseTicketForm from "./components/RaiseTicketForm";
import EmployeeConfirmationCard from "./components/EmployeeConfirmationCard";
import ViewDetails from "./components/ViewDetails";
import ProbationEvaluation from './components/ProbationEvaluation';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<EmployeeConfirmationCard />} />
        <Route path="/view-details/:id" element={<ViewDetails />} />
        <Route path="/probation-evaluation/:id" element={<ProbationEvaluation />} />
      </Routes>
    </Router>
  );
}

export default App;

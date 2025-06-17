import { useParams } from 'react-router-dom';

const ViewDetails = () => {
    const { id } = useParams();

    // fetch employee by ID from API or use static data for now
    // Example:
    // const employee = employees.find(emp => emp.id === parseInt(id));

    return (
        <div>
            <h2>Employee Details for ID: {id}</h2>
            {/* Render details here */}
        </div>
    );
};

export default ViewDetails;

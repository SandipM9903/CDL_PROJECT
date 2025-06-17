import React, { useState, useRef, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom'; // Added useParams and useLocation
import { MdCall } from "react-icons/md";
import ViewDetails from './ViewDetails'; // Assuming ViewDetails component exists and is correctly imported

const EmployeeConfirmation = () => {
    const navigate = useNavigate(); // Re-introduced useNavigate
    const { empId } = useParams(); // Get empId from URL parameters
    const location = useLocation(); // Get current location object

    const [employees, setEmployees] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showViewDetails, setShowViewDetails] = useState(false);
    const [selectedEmployee, setSelectedEmployee] = useState(null);
    const [activeFilter, setActiveFilter] = useState('All');

    // Utility function to format a Date object to DD-MM-YYYY
    const formatDate = (date) => {
        if (!date) return 'N/A';
        const d = new Date(date);
        const day = String(d.getDate()).padStart(2, '0');
        const month = String(d.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed
        const year = d.getFullYear();
        return `${day}-${month}-${year}`;
    };

// Inside your EmployeeConfirmation.jsx component

useEffect(() => {
    const fetchEmployees = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/probation/employees');
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            const transformedData = await Promise.all(data.map(async (item) => {
                let actualProbationEndDate = 'N/A';
                let currentProbationEndDate = 'N/A';
                let probationExtendedNoOfTimes = '0';
                let status = item.status || 'Probation';
                let r1ApprovalStatus = item.r1ApprovalStatus || 'Pending';
                let hrStatus = item.hrStatus || 'Pending';

                if (item.dateOfJoining && typeof item.probationDay === 'number') {
                    const joinDate = new Date(item.dateOfJoining);
                    const probationEnd = new Date(joinDate);
                    probationEnd.setDate(joinDate.getDate() + item.probationDay);
                    actualProbationEndDate = formatDate(probationEnd);
                    currentProbationEndDate = actualProbationEndDate;
                }

                try {
                    const probationResponse = await fetch(`http://localhost:8080/api/probation/probation-record/${item.empCode}`);
                    if (probationResponse.ok) {
                        const record = await probationResponse.json();
                        actualProbationEndDate = formatDate(record.actualProbationEndDate);
                        currentProbationEndDate = formatDate(record.currentProbationEndDate);
                        probationExtendedNoOfTimes = record.totalNumberExtended?.toString() || '0';
                        status = record.status || status;
                        r1ApprovalStatus = record.r1ApprovalStatus || r1ApprovalStatus;
                        hrStatus = record.hrStatus || hrStatus;
                        console.log(`✅ Overridden with probation record for ${item.empCode}`);
                    }
                } catch (err) {
                    // Ignore 404s silently
                    if (!err.message.includes('404')) {
                        console.warn(`⚠️ Failed to fetch probation record for ${item.empCode}:`, err);
                    }
                }

                return {
                    id: item.empId,
                    empCode: item.empCode,
                    profilePic: item.profilePicUrl || 'https://placehold.co/48x48/aabbcc/ffffff?text=EMP',
                    name: `${item.firstName || ''} ${item.lastName || ''}`.trim(),
                    email: item.emailId,
                    role: item.roles,
                    phoneNumber: item.primaryContactNo,
                    rsManager: item.reportingManager,
                    dateOfJoining: formatDate(item.dateOfJoining),
                    probationDays: item.probationDay ? `${item.probationDay} Days` : 'N/A',
                    department: item.department || 'N/A',
                    r1ApprovalStatus,
                    hrStatus,
                    probationExtendedNoOfTimes,
                    confirmationOverdueDays: '0',
                    actualProbationEndDate,
                    currentProbationEndDate,
                    status,
                };
            }));

            setEmployees(transformedData);
        } catch (error) {
            console.error("❌ Error fetching employees:", error);
            setError("Failed to load employees. Please ensure your Spring Boot API is running.");
        } finally {
            setLoading(false);
        }
    };

    fetchEmployees();
}, []);






    // Effect to handle direct URL access for employee details
    useEffect(() => {
        if (!loading && empId && employees.length > 0) {
            const employeeFromUrl = employees.find(emp => String(emp.id) === empId);
            if (employeeFromUrl) {
                setSelectedEmployee(employeeFromUrl);
                setShowViewDetails(true);
            } else {
                // If empId is in URL but employee not found, navigate back
                navigate('/', { replace: true });
            }
        } else if (!loading && !empId && showViewDetails) {
            // If we are on the base path but modal is open, close it
            setShowViewDetails(false);
            setSelectedEmployee(null);
        }
    }, [empId, employees, loading, navigate, showViewDetails]);


   // Updated filter logic
const filteredEmployees = employees.filter(employee => {
    const normalizedStatus = employee.status?.toLowerCase();

    if (activeFilter === 'All') {
        return normalizedStatus === 'probation' || normalizedStatus.includes('extended');
    } else if (activeFilter === 'Probation') {
        return normalizedStatus === 'probation';
    } else if (activeFilter === 'Probation Extended') {
        return normalizedStatus.includes('extended');
    }
    return true;
});

    // EmployeeCard component to render individual employee details
    const EmployeeCard = ({ employee }) => {
        const [showEmailDropdown, setShowEmailDropdown] = useState(false);
        const emailWrapperRef = useRef(null);

        const toggleEmailDropdown = () => {
            setShowEmailDropdown(!showEmailDropdown);
        };

        useEffect(() => {
            function handleClickOutside(event) {
                if (emailWrapperRef.current && !emailWrapperRef.current.contains(event.target)) {
                    setShowEmailDropdown(false);
                }
            }
            document.addEventListener("mousedown", handleClickOutside);
            return () => document.removeEventListener("mousedown", handleClickOutside);
        }, []);

        const handleViewDetails = () => {
            setSelectedEmployee(employee);
            setShowViewDetails(true);
            navigate(`/view-details/${employee.empCode}`);
        };

        return (
            <div className="bg-[#FAFAFA] rounded-[40px] shadow-lg p-4 sm:p-6 mb-6 mx-auto w-full max-w-full md:max-w-4xl lg:max-w-5xl xl:max-w-6xl 2xl:max-w-7xl overflow-hidden">
                {/* Top Section */}
                <div className="flex flex-col sm:flex-row justify-between border-b border-gray-200 pb-4 mb-4 gap-y-4 sm:gap-y-0 min-w-0">
                    {/* Employee Info (Image, Name, Email, Phone, Role) */}
                    <div className="flex items-start sm:items-center gap-4 min-w-0 flex-1">
                        <img src={employee.profilePic || 'https://placehold.co/48x48/aabbcc/ffffff?text=EMP'} alt={employee.name} className="w-12 h-12 rounded-full object-cover flex-shrink-0" />
                        <div className="flex flex-col gap-1 min-w-0 flex-grow">
                            <p className="font-semibold text-lg text-gray-800 truncate">{employee.name}</p>
                            <div className="flex flex-col sm:flex-row items-start sm:items-center gap-x-2 gap-y-1 relative min-w-0" ref={emailWrapperRef}>
                                <div className="flex items-center gap-1 flex-shrink-0 whitespace-nowrap min-w-0">
                                    <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 text-gray-600 flex-shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                                        <path strokeLinecap="round" strokeLinejoin="round" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                    </svg>
                                    <p
                                        className="text-gray-600 text-sm underline-offset-2 hover:underline cursor-pointer truncate"
                                        onClick={toggleEmailDropdown}
                                    >
                                        {employee.email}
                                    </p>
                                </div>

                                <span className="text-gray-400 text-sm hidden sm:inline">|</span>

                                <div className="flex items-center gap-1 flex-shrink-0 whitespace-nowrap">
                                    <MdCall className="text-gray-600 opacity-50 text-sm" />
                                    <span className="text-gray-600 text-sm">{employee.phoneNumber}</span>
                                </div>
                                {showEmailDropdown && (
                                    <div className="absolute z-10 bg-white shadow-lg rounded-md mt-2 w-40 left-0 top-full border border-gray-200">
                                        <a href={`mailto:${employee.email}`} className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" onClick={() => setShowEmailDropdown(false)}>Open Default Client</a>
                                        <a href={`https://mail.google.com/mail/?view=cm&fs=1&to=${employee.email}`} target="_blank" rel="noopener noreferrer" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" onClick={() => setShowEmailDropdown(false)}>Open in Gmail</a>
                                        <a href={`https://outlook.live.com/mail/0/deeplink/compose?to=${employee.email}`} target="_blank" rel="noopener noreferrer" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100" onClick={() => setShowEmailDropdown(false)}>Open in Outlook</a>
                                    </div>
                                )}
                            </div>
                            <p className="text-gray-600 text-sm">{employee.role}</p>
                        </div>
                    </div>
                    {/* Approval Statuses: Stack vertically on mobile, then go side-by-side on sm breakpoint */}
                    <div className="flex flex-col xs:flex-row items-start xs:items-center sm:flex-row sm:items-center sm:justify-end gap-x-6 gap-y-2 flex-wrap sm:flex-nowrap min-w-0">
                        <div className="flex items-center gap-2 flex-shrink-0 min-w-0">
                            <span className="text-gray-600 text-sm whitespace-nowrap">R1 Approval:</span>
                            <span
                                className={`flex items-center px-3 py-1 rounded-full text-sm font-medium whitespace-nowrap
                                ${employee.r1ApprovalStatus === 'Completed'
                                        ? 'bg-green-100 text-green-800 border border-green-500'
                                        : 'bg-orange-100 text-orange-800 border border-orange-500'
                                    }`}
                            >
                                {employee.r1ApprovalStatus === 'Completed' ? (
                                    <span className="mr-1 text-green-600">✓</span>
                                ) : (
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        className="h-4 w-4 mr-1 flex-shrink-0 text-orange-600"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                        stroke="currentColor"
                                        strokeWidth="2"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                                        />
                                    </svg>
                                )}
                                {employee.r1ApprovalStatus}
                            </span>

                        </div>
                        <div className="flex items-center gap-2 flex-shrink-0 min-w-0">
                            <span className="text-gray-600 text-sm whitespace-nowrap">HR Status:</span>
                            <span
                                className={`flex items-center px-3 py-1 rounded-full text-sm font-medium whitespace-nowrap
                                ${employee.hrStatus === 'Completed'
                                        ? 'bg-green-100 text-green-800 border border-green-500'
                                        : 'bg-orange-100 text-orange-800 border border-orange-500'
                                    }`}
                            >
                                {employee.hrStatus === 'Completed' ? (
                                    <span className="mr-1 text-green-600">✓</span>
                                ) : (
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        className="h-4 w-4 mr-1 flex-shrink-0 text-orange-600"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                        stroke="currentColor"
                                        strokeWidth="2"
                                    >
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                                        />
                                    </svg>
                                )}
                                {employee.hrStatus}
                            </span>


                        </div>
                    </div>
                </div>

                {/* Middle Section - Probation Details */}
                <div className="grid grid-cols-1 xs:grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-7 gap-y-6 gap-x-4 text-sm text-gray-700 border-b border-gray-300 pb-5 mb-6">
                    <div>
                        <p className="text-gray-500 text-xs">Department</p>
                        <p className="text-gray-800 font-semibold break-words">{employee.department}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs">R1 Manager</p>
                        <p className="text-gray-900 font-bold break-words">{employee.rsManager}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs">Date of Joining</p>
                        <p className="text-gray-800 font-medium break-words">{employee.dateOfJoining}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs">Probation Days</p>
                        <p className="text-gray-900 font-bold break-words">{employee.probationDays}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs">Actual End Date</p>
                        <p className="text-gray-800 font-medium break-words">{employee.actualProbationEndDate}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs">Extended No. of Times</p>
                        <p className="text-gray-800 font-medium break-words">{employee.probationExtendedNoOfTimes}</p>
                    </div>
                    <div>
                        <p className="text-gray-500 text-xs">Status</p>
                        <p className="text-gray-900 font-bold break-words">{employee.status}</p>
                    </div>
                </div>

                {/* Bottom Section */}
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 text-sm items-center">
                    <div className="sm:col-span-1 text-center">
                        <p className="text-gray-500">Confirmation Overdue in Days</p>
                        <p className="text-red-500 text-xl font-bold">{employee.confirmationOverdueDays}</p>
                    </div>

                    <div className="sm:col-span-1 text-center">
                        <p className="text-gray-500">Current Probation End Date</p>
                        <p className="text-gray-800 font-medium break-words">{employee.currentProbationEndDate}</p>
                    </div>

                    <div className="flex justify-center sm:justify-end sm:col-span-1">
                        <button className="text-red-600 font-semibold flex items-center gap-1 hover:text-red-700 transition duration-200" onClick={handleViewDetails}>
                            View Details
                            <span className="text-lg">→</span>
                        </button>

                    </div>
                </div>
            </div>
        );
    };

    const handleCloseViewDetails = () => {
        setShowViewDetails(false);
        setSelectedEmployee(null);
        navigate('/', { replace: true }); // Navigate back to the base path when modal closes
    };

    return (
        <div className="min-h-screen bg-white py-6 font-sans overflow-x-hidden">
            <div className="max-w-7xl mx-auto px-4 sm:px-8">
                <h1 className="text-3xl font-semibold text-gray-900 mb-6 text-center sm:text-left">Employee Confirmation</h1>

                {/* Search and Filter */}
                <div className="flex flex-col sm:flex-row gap-3 items-stretch sm:items-center mb-4 px-2 sm:px-0">
                    <div className="relative flex-grow sm:flex-grow-0 sm:w-80 max-w-full">
                        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                        </span>
                        <input
                            type="text"
                            placeholder="Search Employees"
                            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>
                    <button className="flex-shrink-0 flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md text-gray-700 bg-white hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-300 w-full sm:w-auto max-w-full">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293-.707V19l-4 4v-3.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
                        </svg>
                        Advance Filter
                    </button>
                </div>

                {/* Tabs */}
                <div className="flex flex-wrap gap-3 mb-8 px-2 sm:px-0">
                    <button
                        className={`px-5 py-2 rounded-full font-medium whitespace-nowrap flex-shrink-0 ${activeFilter === 'All' ? 'bg-[#6bf6bf] text-black border border-black' : 'bg-white text-black border border-gray-800 hover:bg-gray-100'}`}
                        onClick={() => setActiveFilter('All')}
                    >
                        All
                    </button>
                    <button
                        className={`px-5 py-2 rounded-full font-medium whitespace-nowrap flex-shrink-0 ${activeFilter === 'Probation' ? 'bg-[#6bf6bf] text-black border border-black' : 'bg-white text-black border border-gray-800 hover:bg-gray-100'}`}
                        onClick={() => setActiveFilter('Probation')}
                    >
                        Probation
                    </button>
                    <button
                        className={`px-5 py-2 rounded-full font-medium whitespace-nowrap flex-shrink-0 ${activeFilter === 'Probation Extended' ? 'bg-[#6bf6bf] text-black border border-black' : 'bg-white text-black border border-gray-800 hover:bg-gray-100'}`}
                        onClick={() => setActiveFilter('Probation Extended')}
                    >
                        Probation Extended
                    </button>
                </div>

                {/* Employee List */}
                <div className="space-y-6 flex flex-col items-center">
                    {loading && <p className="text-gray-700 text-lg">Loading employees...</p>}
                    {error && <p className="text-red-600 text-lg">{error}</p>}
                    {!loading && !error && filteredEmployees.length === 0 && (
                        <p className="text-gray-700 text-lg">No employees found for the selected filter.</p>
                    )}
                    {!loading && !error && filteredEmployees.map((employee) => (
                        <EmployeeCard key={employee.id} employee={employee} />
                    ))}
                </div>
            </div>

            {/* View Details Modal */}
            {showViewDetails && selectedEmployee && (
                <ViewDetails
                    employee={selectedEmployee}
                    onClose={handleCloseViewDetails} // Use the new handler
                />
            )}
        </div>
    );
};

export default EmployeeConfirmation;

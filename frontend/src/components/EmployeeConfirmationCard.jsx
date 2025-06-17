import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { MdCall } from "react-icons/md";
import ViewDetails from './ViewDetails';

const EmployeeConfirmation = () => {
    const navigate = useNavigate();
    const [showViewDetails, setShowViewDetails] = useState(false);
    const [selectedEmployee, setSelectedEmployee] = useState(null);
    const [activeFilter, setActiveFilter] = useState('All');

    const employees = [
        {
            id: 1,
            profilePic: 'https://randomuser.me/api/portraits/thumb/men/1.jpg',
            name: 'Mehul Shaikh',
            email: 'mehul_shaikh@cms.co.in',
            role: 'Sr JAVA developer',
            phoneNumber: '8886979270',
            r1ApprovalStatus: 'Completed',
            hrStatus: 'Pending',
            department: 'SSD',
            rsManager: 'Harish kumar',
            dateOfJoining: '16-04-2025',
            probationDays: '90 Days',
            actualProbationEndDate: '17-07-2025',
            probationExtendedNoOfTimes: '0',
            status: 'Probation',
            confirmationOverdueDays: '3',
            currentProbationEndDate: '17-07-2025',
        },
        {
            id: 2,
            profilePic: 'https://via.placeholder.com/48',
            name: 'Jane Doe',
            email: 'jane.doe@example.com',
            role: 'Frontend Developer',
            phoneNumber: '9876543210',
            r1ApprovalStatus: 'Completed',
            hrStatus: 'Completed',
            department: 'Web Dev',
            rsManager: 'Alice Smith',
            dateOfJoining: '01-03-2024',
            probationDays: '60 Days',
            actualProbationEndDate: '30-04-2024',
            probationExtendedNoOfTimes: '0',
            status: 'Confirmed',
            confirmationOverdueDays: '0',
            currentProbationEndDate: '30-04-2024',
        },
        {
            id: 3,
            profilePic: 'https://via.placeholder.com/48',
            name: 'John Smith',
            email: 'john.smith@example.com',
            role: 'Backend Engineer',
            phoneNumber: '7778889990',
            r1ApprovalStatus: 'Pending',
            hrStatus: 'Pending',
            department: 'Backend',
            rsManager: 'Bob Johnson',
            dateOfJoining: '10-01-2025',
            probationDays: '90 Days',
            actualProbationEndDate: '10-04-2025',
            probationExtendedNoOfTimes: '1',
            status: 'Probation Extended',
            confirmationOverdueDays: '15',
            currentProbationEndDate: '25-05-2025',
        },
        {
            id: 4,
            profilePic: 'https://via.placeholder.com/48',
            name: 'Preethi Sharma',
            email: 'preethi.sharma@cms.co.in',
            role: 'Senior Java developer',
            phoneNumber: '9629680844',
            r1ApprovalStatus: 'Completed',
            hrStatus: 'Pending',
            department: 'Software Solutions & Delivery',
            rsManager: 'Harish Kumar',
            dateOfJoining: '01-02-2024',
            probationDays: '90 Days',
            actualProbationEndDate: '01-05-2024',
            probationExtendedNoOfTimes: '3',
            status: 'Probation Extended',
            confirmationOverdueDays: '2',
            currentProbationEndDate: '01-05-2025',
        },
    ];

    const filteredEmployees = employees.filter(employee => {
        if (activeFilter === 'All') {
            return employee.status === 'Probation' || employee.status === 'Probation Extended';
        } else if (activeFilter === 'Probation') {
            return employee.status === 'Probation';
        } else if (activeFilter === 'Probation Extended') {
            return employee.status === 'Probation Extended';
        }
        return true;
    });

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
            navigate(`/view-details/${employee.id}`);
        };

        return (
            <div className="bg-[#FAFAFA] rounded-[40px] shadow-lg p-4 sm:p-6 mb-6 mx-auto w-full max-w-full md:max-w-4xl lg:max-w-5xl xl:max-w-6xl 2xl:max-w-7xl overflow-hidden">
                {/* Top Section */}
                <div className="flex flex-col sm:flex-row justify-between border-b border-gray-200 pb-4 mb-4 gap-y-4 sm:gap-y-0 min-w-0">
                    {/* Employee Info (Image, Name, Email, Phone, Role) */}
                    <div className="flex items-start sm:items-center gap-4 min-w-0 flex-1">
                        <img src={employee.profilePic} alt={employee.name} className="w-12 h-12 rounded-full object-cover flex-shrink-0" />
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
                        <p className="text-gray-500 text-xs">RS Manager</p>
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
                        <button class="text-red-600 font-semibold flex items-center gap-1 hover:text-red-700 transition duration-200" onClick={handleViewDetails}>
  View Details
  <span class="text-lg">→</span>
</button>

                    </div>
                </div>
            </div>
        );
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
                    {filteredEmployees.map((employee) => (
                        <EmployeeCard key={employee.id} employee={employee} />
                    ))}
                </div>
            </div>

            {/* View Details Modal */}
            {showViewDetails && selectedEmployee && (
                <ViewDetails
                    employee={selectedEmployee}
                    onClose={() => setShowViewDetails(false)}
                />
            )}
        </div>
    );
};

export default EmployeeConfirmation;
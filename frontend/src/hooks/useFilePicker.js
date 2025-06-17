import { useRef } from 'react';

const useFilePicker = (onFileSelect) => {
    const fileInputRef = useRef(null);

    const triggerFileInput = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    };

    const handleFileChange = (event) => {
        const files = event.target.files;
        if (onFileSelect && files.length > 0) {
            onFileSelect(files);
        }
    };

    const handleDrop = (event) => {
        event.preventDefault();
        event.stopPropagation();
        const files = event.dataTransfer.files;
        if (onFileSelect && files.length > 0) {
            onFileSelect(files);
        }
    };

    const handleDragOver = (event) => {
        event.preventDefault();
        event.stopPropagation();
    };

    const FileInput = (
        <input
            type="file"
            ref={fileInputRef}
            onChange={handleFileChange}
            style={{ display: 'none' }}
        />
    );

    return {
        triggerFileInput,
        handleDrop,
        handleDragOver,
        FileInput,
    };
};

export default useFilePicker;

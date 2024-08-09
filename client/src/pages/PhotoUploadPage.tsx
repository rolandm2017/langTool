import React, { useState, useCallback } from "react"
import { useDropzone, FileRejection } from "react-dropzone"

interface PhotoUploaderProps {
    maxFiles?: number
}

const PhotoUploaderPage: React.FC<PhotoUploaderProps> = ({ maxFiles = 20 }) => {
    const [files, setFiles] = useState<File[]>([])

    const onDrop = useCallback(
        (acceptedFiles: File[], fileRejections: FileRejection[]) => {
            setFiles((prevFiles) =>
                [...prevFiles, ...acceptedFiles].slice(0, maxFiles)
            )
            uploadFiles(acceptedFiles)
        },
        [maxFiles]
    )

    const { getRootProps, getInputProps, isDragActive } = useDropzone({
        onDrop,
        accept: {
            "image/*": [],
        },
        maxFiles,
        multiple: true,
    })

    const uploadFiles = async (filesToUpload: File[]) => {
        const formData = new FormData()
        filesToUpload.forEach((file) => {
            formData.append("photos", file)
        })

        try {
            const response = await fetch("YOUR_UPLOAD_API_ENDPOINT", {
                method: "POST",
                body: formData,
            })

            if (response.ok) {
                console.log("Files uploaded successfully")
                // Handle successful upload (e.g., show a success message)
            } else {
                console.error("Upload failed")
                // Handle upload failure
            }
        } catch (error) {
            console.error("Error uploading files:", error)
            // Handle error
        }
    }

    return (
        <div>
            <div {...getRootProps()} style={dropzoneStyles}>
                <input {...getInputProps()} />
                {isDragActive ? (
                    <p>Drop the files here ...</p>
                ) : (
                    <p>
                        Drag 'n' drop some files here, or click to select files
                    </p>
                )}
            </div>
            {files.length > 0 && (
                <div>
                    <div>
                        <h4>Files to upload:</h4>
                        <ul>
                            {files.map((file) => (
                                <li key={file.name}>{file.name}</li>
                            ))}
                        </ul>
                    </div>
                    <div>
                        <button>Upload</button>
                    </div>
                </div>
            )}
        </div>
    )
}

const dropzoneStyles: React.CSSProperties = {
    border: "2px dashed #cccccc",
    borderRadius: "4px",
    padding: "20px",
    textAlign: "center",
    cursor: "pointer",
}

export default PhotoUploaderPage

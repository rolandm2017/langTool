import React, { useState, useCallback, CSSProperties } from "react"
import { useDropzone, FileRejection } from "react-dropzone"
import usePhotoUpload from "../api/useUploadPhotos.api"

interface PhotoUploaderProps {
    maxFiles?: number
}

const PhotoUploaderPage: React.FC<PhotoUploaderProps> = ({ maxFiles = 20 }) => {
    const [files, setFiles] = useState<File[]>([])

    const { uploadPhotos, progress, isUploading, error } = usePhotoUpload()

    const onDrop = useCallback(
        (acceptedFiles: File[], fileRejections: FileRejection[]) => {
            setFiles((prevFiles) =>
                [...prevFiles, ...acceptedFiles].slice(0, maxFiles)
            )
            // todo: show GUI displaying files, require "confirm ok" button press
            startUploadFiles(acceptedFiles)
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

    const startUploadFiles = async (filesToUpload: File[]) => {
        // const formData = new FormData()
        // filesToUpload.forEach((file) => {
        // formData.append("photos", file)
        // })
        uploadPhotos(filesToUpload)
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

const dropzoneStyles: CSSProperties = {
    border: "2px dashed #cccccc",
    borderRadius: "4px",
    padding: "20px",
    textAlign: "center",
    cursor: "pointer",
}

export default PhotoUploaderPage

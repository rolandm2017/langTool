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
            // todo: update presentation of  GUI displaying files, require "confirm ok" button press
            // startUploadFiles(acceptedFiles)
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

    const startUploadFiles = () => {
        // const startUploadFiles = async (filesToUpload: File[]) => {
        // const formData = new FormData()
        // filesToUpload.forEach((file) => {
        // formData.append("photos", file)
        // })
        uploadPhotos(files)
    }

    return (
        <div>
            <div style={styles.container}>
                <div {...getRootProps()} style={dropzoneStyles}>
                    <input {...getInputProps()} />
                    {isDragActive ? (
                        <p>Drop the files here ...</p>
                    ) : (
                        <p>
                            Drag 'n' drop some files here, or click to select
                            files
                        </p>
                    )}
                </div>
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
                        <button
                            className="mt-2 bg-gray-200 shadow-md"
                            onClick={() => {
                                startUploadFiles()
                            }}
                        >
                            Start Upload
                        </button>
                    </div>
                </div>
            )}
        </div>
    )
}

const dropzoneStyles: CSSProperties = {
    margin: "40px 0 0 0",
    border: "2px dashed #cccccc",
    borderRadius: "4px",
    textAlign: "center",
    cursor: "pointer",
    height: "120px",
    // center text.
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
}

const styles = {
    container: {
        margin: "40px 0 20px 0",
        padding: "0 30px 40px 30px",
        border: "2px dashed black",
    },
    title: {
        fontSize: "2.5em",
        marginBottom: "20px",
    },
}

export default PhotoUploaderPage

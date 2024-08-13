import React, { useState, useCallback, CSSProperties, useEffect } from "react"
import { useDropzone, FileRejection } from "react-dropzone"
import usePhotoUpload from "../api/useUploadPhotos.api"
import UploadProgress from "@/components/UploadProgress"
import useGetNextCollectionId from "@/api/useGetNextCollectionId.api"

interface PhotoUploaderProps {
    maxFiles?: number
}

const PhotoUploaderPage: React.FC<PhotoUploaderProps> = ({ maxFiles = 20 }) => {
    const [files, setFiles] = useState<File[]>([])

    const {
        nextCollectionId,
        loading: collectionIdIsLoading,
        error: collectionIdError,
    } = useGetNextCollectionId()

    let reportedCollectionId = nextCollectionId ?? -1 // appeasing typescript

    useEffect(() => {
        // appeasing typescript
        const collectionIdIsLoaded = !collectionIdIsLoading
        if (collectionIdIsLoaded && nextCollectionId !== null) {
            reportedCollectionId = nextCollectionId
        }
    }, [collectionIdIsLoading, nextCollectionId])

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

    const idHasLoaded = (value: any): value is number =>
        typeof value === "number"

    const startUploadFiles = () => {
        // const startUploadFiles = async (filesToUpload: File[]) => {
        // const formData = new FormData()
        // filesToUpload.forEach((file) => {
        // formData.append("photos", file)
        // })
        if (idHasLoaded(nextCollectionId)) {
            uploadPhotos(files, reportedCollectionId)
        }
        // todo: show error if not has loaded
    }

    useEffect(() => {
        if (progress === 100) {
            clearFiles()
        }
    }, [progress])

    function clearFiles() {
        setFiles([])
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
                    {/* todo: a label input box, optional */}

                    <div className="flex justify-center mb-4">
                        <div className="mr-2">
                            {!collectionIdIsLoading &&
                                nextCollectionId !== null && (
                                    <button
                                        className="mt-2 bg-gray-200 shadow-md"
                                        onClick={() => {
                                            startUploadFiles()
                                        }}
                                    >
                                        Start Upload
                                        {/* TODO: mark files uploaded after they're uploaded, give a "Clear" box */}
                                    </button>
                                )}
                        </div>
                        <div className="ml-2 mr-6">
                            {/* <button
                                className="mt-2 bg-gray-200 shadow-md"
                                onClick={clearFiles}
                            >
                                Clear
                            </button> */}
                        </div>
                    </div>
                    <div className="flex justify-center mx-12 border-4 border-red-300">
                        <div className="w-40 h-12 border-4 border-sky-300 flex items-center">
                            <UploadProgress progress={progress} />
                        </div>
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

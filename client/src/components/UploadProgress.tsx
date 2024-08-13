import React from "react"

interface UploadProgressProps {
    progress: number
}

const UploadProgress: React.FC<UploadProgressProps> = ({ progress }) => {
    // Ensure progress is between 0 and 100
    const clampedProgress = Math.min(Math.max(progress, 0), 100)

    return (
        <div className="w-full h-4 bg-gray-200 rounded-full overflow-hidden">
            <div
                className="h-full bg-blue-500 transition-all duration-300 ease-in-out"
                style={{ width: `${clampedProgress}%` }}
            />
            <p>{progress < 100 ? "Sending..." : "Done"}</p>
        </div>
    )
}

export default UploadProgress

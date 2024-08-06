import React, { useState } from "react"
import { BrowserRouter as Router, Route, Link, Routes } from "react-router-dom"

import AnkiDeckDownloadPage from "./pages/AnkiDeckDownloadPage"
import PhotoUploaderPage from "./pages/PhotoUploadPage"
import PhotoSetPreviewPage from "./pages/PhotoSetPreviewPage"
import SwipeUiPage from "./pages/SwipeUiPage"
import WordRatingPage from "./pages/WordRatingPage"

import "./App.css"

const routesOptions = {
    wordRating: "/wordRating",
    photoUpload: "/photoUpload",
    photoSetPreview: "/photoSetPreview",
    ankiDeckDownload: "/ankiDeckDownload",
    swipeUi: "/swipeUiPage",
}

function App() {
    const [count, setCount] = useState(0)

    return (
        <Router>
            <div>
                <nav>
                    <ul>
                        <li>
                            <Link to={routesOptions.wordRating}>
                                Word Rating
                            </Link>
                        </li>
                        <li>
                            <Link to={routesOptions.photoSetPreview}>
                                Photo Set Preview
                            </Link>
                        </li>
                        <li>
                            <Link to={routesOptions.photoUpload}>
                                Photo Upload
                            </Link>
                        </li>
                    </ul>
                </nav>

                <Routes>
                    <Route
                        path={routesOptions.ankiDeckDownload}
                        element={<AnkiDeckDownloadPage />}
                    />
                    <Route
                        path={routesOptions.photoSetPreview}
                        element={<PhotoSetPreviewPage />}
                    />
                    <Route
                        path={routesOptions.photoUpload}
                        element={<PhotoUploaderPage />}
                    />
                    <Route
                        path={routesOptions.swipeUi}
                        element={<SwipeUiPage />}
                    />
                    <Route
                        path={routesOptions.wordRating}
                        element={<WordRatingPage />}
                    />
                </Routes>
            </div>
        </Router>
    )
}

export default App

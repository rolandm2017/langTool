import React, { useState } from "react"
import { BrowserRouter as Router, Route, Link, Routes } from "react-router-dom"

import LandingPage from "./pages/LandingPage"
import AnkiDeckDownloadPage from "./pages/AnkiDeckDownloadPage"
import PhotoUploaderPage from "./pages/PhotoUploadPage"
import PhotoCollectionPreviewPage from "./pages/PhotoCollectionPreviewPage"
import SwipeUiPage from "./pages/SwipeUiPage"
import WordRatingPage from "./pages/WordRatingPage"

import NavigationEntry from "./components/NavigationEntry"

import "./App.css"

const routesOptions = {
    homeUrl: "/", // landing page
    wordRating: "/wordRating", // pg 1 in hierarchy
    swipeUi: "/swipeUiPage", // pg 2 in hierarchy
    photoUpload: "/photoUpload", // pg 3 in hierarchy
    photoCollectionPreview: "/photoCollectionPreview", // pg 4 in hierarchy
    ankiDeckDownload: "/ankiDeckDownload", // pg 5 in hierarchy
}

function App() {
    const [count, setCount] = useState(0)

    return (
        <Router>
            <div>
                <nav>
                    <ul
                        style={{
                            display: "flex",
                            flexDirection: "row",
                            listStyle: "none",
                            padding: 0,
                            margin: 0,
                        }}
                    >
                        <NavigationEntry toUrl={routesOptions.homeUrl}>
                            Landing Page
                        </NavigationEntry>

                        <NavigationEntry toUrl={routesOptions.wordRating}>
                            Word Rating
                        </NavigationEntry>
                        <NavigationEntry toUrl={routesOptions.swipeUi}>
                            Swipe to Sort
                        </NavigationEntry>

                        <NavigationEntry toUrl={routesOptions.photoUpload}>
                            Photo Upload
                        </NavigationEntry>
                        <NavigationEntry
                            toUrl={routesOptions.photoCollectionPreview}
                        >
                            Photo Set Preview
                        </NavigationEntry>
                        <NavigationEntry toUrl={routesOptions.ankiDeckDownload}>
                            Anki Deck Downloader
                        </NavigationEntry>
                    </ul>
                </nav>
                {/* 
                <div>
                    <div>
                        <h1>Photo to Anki Deck Converter</h1>
                    </div>
                    <div>
                        <div>
                            <h2>Stop wasting hours and hours </h2>
                        </div>
                        <div>
                            <h2>Hand crafting decks is a chore</h2>
                        </div>
                        <div>
                            <h2>Let the software do it for you</h2>
                        </div>
                        <div>
                            <h2>Generate decks of only unknown words</h2>
                        </div>
                        <div>
                            <button>Sign up today</button>
                        </div>
                    </div>
                </div> */}

                <Routes>
                    <Route
                        path={routesOptions.homeUrl}
                        element={<LandingPage />}
                    />
                    <Route
                        path={routesOptions.ankiDeckDownload}
                        element={<AnkiDeckDownloadPage />}
                    />
                    <Route
                        path={routesOptions.photoCollectionPreview}
                        element={<PhotoCollectionPreviewPage />}
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

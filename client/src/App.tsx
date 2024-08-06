import React from 'react';
import { BrowserRouter as Router, Route, Link, Routes } from 'react-router-dom';

import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

const Foo = () => <h2>Foo Page</h2>;
const Bar = () => <h2>Bar Page</h2>;
const Baz = () => <h2>Baz Page</h2>;

const routesOptions = {wordRating: "/wordRating", photoUpload: "/photoUpload", photoSetPreview: "/photoSetPreview", ankiDeckDownload: "/ankiDeckDownload"}

function App() {
  const [count, setCount] = useState(0)

  return (
    <Router>
      <div>
        <nav>
          <ul>
          <li>
              <Link to={routesOptions.wordRating}>Word Rating</Link>
            </li>
            <li>
              <Link to={routesOptions.photoSetPreview}>Photo Set Preview</Link>
            </li>
            <li>
              <Link to={routesOptions.photoUpload}>Photo Upload</Link>
            </li>
          </ul>
        </nav>

        <Routes>
          <Route path={routesOptions.wordRating} element={<Foo />} />
          <Route path={routesOptions.photoSetPreview} element={<Bar />} />
          <Route path={routesOptions.photoUpload} element={<Baz />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App

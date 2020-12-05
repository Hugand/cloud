import React from 'react'
import '../../styles/blocks/sidebar.scss'

function SideBar(props) {
    return (
        <section className="sidebar">
            <section className="cloud-space-container">
                <div className="space—usage-display">
                    <div className="labels">
                        <h1>25.3GB</h1>
                        <p>10GB available</p>
                    </div>
                    <div className="bar">
                        <div className="docs"></div>
                        <div className="vids"></div>
                        <div className="imgs"></div>
                    </div>
                </div>
                <div className="file-type-desc">
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Image</label>
                        <label className="space-used light-text">10GB</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Videos</label>
                        <label className="space-used light-text">7GB</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Docs</label>
                        <label className="space-used light-text">6GB</label>
                    </div>
                    <div className="file">
                        <div className="file-type-icon"></div>
                        <label className="file-type dark-text">Available</label>
                        <label className="space-used light-text">10GB</label>
                    </div>
                </div>
            </section>
            <section className="file-info-container">
                <header>
                    <div className="id-row">
                        <div className="file-type-icon"></div>
                        <h2 className="file-name">File name</h2>
                        <label className="file-size light-text">8KB</label>
                    </div>
                    <p className="file-last-mod">Last modified Sep 3, 2019</p>
                </header>
                <div className="action-buttons">
                    <button className="action-btn">
                        <img src="./assets/icons/download_icon.svg" alt="" /> <label className="dark-text">Download</label>
                    </button>
                    <button className="action-btn">
                        <img src="./assets/icons/rename_icon.svg" alt="" /> <label className="dark-text">Rename</label>
                    </button>
                    <button className="action-btn">
                        <img src="./assets/icons/move_icon.svg" alt="" /> <label className="dark-text">Move</label>
                    </button>
                    <div className="separator"></div>
                    <button className="action-btn delete-btn">
                        <img src="./assets/icons/delete_icon.svg" alt="" /> <label className="dark-text">Delete</label>
                    </button>
                </div>
            </section>
        </section>
    )
}

export default SideBar
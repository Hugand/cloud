import React from 'react'
import { formatDate } from '../../helpers/file'

/*
    @props {Object} selectedFileActions
*/
function FileActions({ selectedFileActions }) {
    return (
        <section className="file-info-container">
            <header>
                <div className="id-row">
                    <div className="file-type-icon"></div>
                    <h2 className="file-name">{ selectedFileActions.file_name }</h2>
                </div>
                <label className="file-size light-text">{ selectedFileActions.file_size }</label>
                <p className="file-last-mod">Last modified { formatDate(selectedFileActions.file_created_at) }</p>
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
    )
}

export default FileActions
import React, { useState } from 'react'
import { formatDate } from '../../helpers/file'
import useFileOperations from '../../hooks/fileOperationsHook'
import { useStateValue } from '../../state'
import '../../styles/blocks/file-actions.scss'


/*
    @props {Object} selectedFileActions
*/
function FileActions() {
    const { deleteFile, renameFile } = useFileOperations()
    const [ isRenameActive, setIsRenameActive ] = useState(false)
    const [ newName, setNewName ] = useState("")
    const [ { selectedFileActions }, dispatch ] = useStateValue()

    async function handleDeleteFile(e) {
        deleteFile(selectedFileActions.file_name)
        dispatch({
            type: 'changeSelectedFileActions',
            value: null
        })
    }

    function handleMoveFileClick() {
        dispatch({
            type: 'changeDisplayMoveFileModal',
            value: true
        })
    }

    function handleRenameFile(e) {
        renameFile(selectedFileActions.file_name, newName)
        dispatch({
            type: 'changeSelectedFileActions',
            value: null
        })
    }

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
                
                { !isRenameActive
                    ? <button className="action-btn" onClick={() => setIsRenameActive(true)}>
                        <img src="./assets/icons/rename_icon.svg" alt="" /> <label className="dark-text">Rename</label>
                    </button>
                    : <div className="rename-field-container">
                        <input type="text" className="rename-field" onChange={e => setNewName(e.target.value)}/>
                        <button 
                            className="confirm-btn"
                            onClick={handleRenameFile}>
                                <img src="./assets/icons/tick_icon.svg" alt='' /></button>
                </div> }

                <button className="action-btn" onClick={handleMoveFileClick}>
                    <img src="./assets/icons/move_icon.svg" alt="" /> <label className="dark-text">Move</label>
                </button>

                <div className="separator"></div>

                <button className="action-btn delete-btn" onClick={handleDeleteFile}>
                    <img src="./assets/icons/delete_icon.svg" alt=""/> <label className="dark-text">Delete</label>
                </button>
            </div>
        </section>
    )
}

export default FileActions
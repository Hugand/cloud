import React from 'react'
import API from '../../helpers/Api'
import '../../styles/atoms/actions-popup.scss'

/*
    @props {File} file
    @props {Function} deleteFile
*/
function ActionsPopUp(props) {

    function deleteFile() {
        props.deleteFile(props.file.file_name)
    }

    return (
        <div className="actions-popup-container">
            <button className="dark-text popup-action-btn">Rename</button>
            <button className="dark-text popup-action-btn">Move</button>
            <button className="popup-action-btn delete-btn" onClick={deleteFile}>Delete</button>
        </div>
    )
}

export default ActionsPopUp
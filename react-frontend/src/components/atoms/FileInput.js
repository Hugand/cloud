import React, { useState } from 'react'
import { getFileSize } from '../../helpers/file'
import '../../styles/atoms/file-input.scss'

/*
    @props {Function} setFile
    @props {File} file
*/
function FileInput(props) {
    return (
        <div className="file-input-container">
            <label className="file-input" htmlFor="file-inp">
                <input id="file-inp" type="file" onChange={
                    e => {
                        props.setFile(e.target.files[0])
                        console.log(e.target.files[0])
                    } } />
                <p>{ props.file ? props.file.name : 'Choose a file...' }</p>
                <label className="btn-primary" htmlFor="file-inp">Browse</label>
            </label>
            <label className="size-label">{ props.file ? getFileSize(props.file.size) : '0KB' }</label>
        </div>)
}

export default FileInput
import React, { useState } from 'react'
import API from '../../../helpers/Api'
import FileInput from '../../atoms/FileInput'
import '../../../styles/blocks/upload-file-box.scss'
import { useStateValue } from '../../../state'

/*
    @props {Function} handleModalToggle
*/
function UploadFileBox(props) {
    const GREEN = "#4e9e3f"
    const RED = "#c94444"

    const [ { dirs }, _ ] = useStateValue()

    const [ file, setFile ] = useState()
    const [ status, setStatus ] = useState({
        msg: "",
        color: GREEN
    })

    // TODO: Put this function in the fileSomething hook
    async function uploadNewFile(e) {
        e.preventDefault()

        let reqBody = new FormData()
        reqBody.append("file", file)
        reqBody.append("dir", dirs.join('/')+'/')

        setStatus({
            msg: "Uploading file...",
            color: GREEN
        })

        API.submitNewFile(reqBody)
            .then(res => {
                console.log(res)
                switch(res.status) {
                    case 'success':
                        setStatus({  msg: "Done!", color: GREEN })
                        props.handleModalToggle(false)
                        break
                    case 'error':
                    case 'failed':
                    default:
                        throw res
                }
            })
            .catch( err => {
                const newStatus = {
                    msg: "",
                    color: RED
                }

                switch(err.desc) {
                    case 'FILE_ALREADY_EXISTS':
                        newStatus.msg = 'Error: File already exists!'
                        break
                    case 'PATH_INVALID':
                        newStatus.msg = 'Error: Path invalid!'
                        break
                    default: newStatus.msg = 'Error!'
                }

                setStatus(newStatus)
            })
    }

    return (
        <>
            <h1 className="modal-title">Upload file</h1>
            {/* <input type="file" className="input-file" onChange={e => setFile(e.target.files[0])}/> */}
            <FileInput setFile={setFile} file={file}/>
            <div className="modal-actions">
                <button className="btn-primary" onClick={uploadNewFile}>Upload file</button>
                <label className="status-label" style={{color: status.color}}>{ status.msg }</label>
            </div>
        </>
    )
}

export default UploadFileBox
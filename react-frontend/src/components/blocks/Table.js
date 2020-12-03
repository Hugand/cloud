import React from 'react'
import '../../styles/blocks/table.scss'

/*
    @props {array} data
    @props {function} goBack
    @props {function} navigateToDir
    @props {function} deleteFile
    @props {array} currDir
*/

function Table(props) {
    return (
        <div className="table-container">
            <div className="table-actions">
                <button onClick={props.goBack}><img src="./assets/icons/back_arrow_icon.svg" alt="" /></button>
                <p className="dark-text">{"./" + props.currDir.join("/")}</p>
            </div>
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th>Name</th>
                        <th>Last modified</th>
                        <th>Size</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                {
                    props.data.map(file => 
                    <tr key={file.file_name}
                        onClick={() => file.type !== "file" && props.navigateToDir(file.file_name)}>
                        <td><div className="file-type"></div></td>
                        <td className="dark-text">{file.file_name}</td>
                        
                        <td className="light-text">{(new Date(file.file_created_at)).toDateString().split(" ").slice(1).join(" ")}</td>
                        <td className="light-text">{file.file_size}</td>
                        <td>
                            <button className="more-btn" onClick={() => props.deleteFile(file.file_name)}>
                                <img src="./assets/icons/three_dot_icon.svg" alt="" />
                            </button>
                        </td>
                    </tr>)
                }
                </tbody>
            </table>
        </div>
    )
}

export default Table
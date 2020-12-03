import React, { useState } from 'react'
import '../../styles/blocks/table.scss'
import ActionsPopUp from '../atoms/ActionsPopUp'

/*
    @props {array} data
    @props {function} goBack
    @props {function} navigateToDir
    @props {function} deleteFile
    @props {array} currDir
*/

function Table(props) {
    const [ selectedMoreRow, setSelectedMoreRow ] = useState(-1)

    function formatDate(date) { return (new Date(date)).toDateString().split(" ").slice(1).join(" ")}

    function handleRowsMoreOptionsClick(e, pos) {
        if(pos === selectedMoreRow)
            setSelectedMoreRow(-1)
        else
            setSelectedMoreRow(pos)

        e.stopPropagation()
    }

    function handleNavigateDirClick(file) {
        if(file.type !== "file") {
            setSelectedMoreRow(-1)
            props.navigateToDir(file.file_name)
        }

    }

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
                    props.data && props.data.map((file, i) => 
                    <tr key={file.file_name}
                        onClick={() => handleNavigateDirClick(file)}>
                        <td><div className="file-type"></div></td>
                        <td className="dark-text">{ file.file_name }</td>
                        
                        <td className="light-text">{ formatDate(file.file_created_at) }</td>
                        <td className="light-text">{ file.file_size }</td>
                        <td>
                            <button className="more-btn" onClick={e => handleRowsMoreOptionsClick(e, i)}>
                                <img src="./assets/icons/three_dot_icon.svg" alt="" />
                                { selectedMoreRow === i && <ActionsPopUp /> }
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
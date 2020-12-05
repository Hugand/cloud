import React, { useState } from 'react'
import useFileOperations from '../../hooks/fileOperationsHook'
import { useStateValue } from '../../state'
import '../../styles/blocks/table.scss'
import ActionsPopUp from '../atoms/ActionsPopUp'

/*
    @props {array} data
*/

function Table(props) {
    const [ { dirs }, _ ] = useStateValue()
    const { goBack, navigateToDir } = useFileOperations()
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
            navigateToDir(file.file_name)
        }
    }

    return (
        <div className="table-container">
            <div className="table-actions">
                <button onClick={goBack}><img src="./assets/icons/back_arrow_icon.svg" alt="" /></button>
                <p className="dark-text">{"./" + dirs.join("/")}</p>
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
                            <span className="more-btn" onClick={e => handleRowsMoreOptionsClick(e, i)}>
                                <img src="./assets/icons/three_dot_icon.svg" alt="" />

                                { selectedMoreRow === i &&
                                    <ActionsPopUp
                                        file={ file }
                                        handlePopupDisplay={setSelectedMoreRow} /> }
                            </span>
                        </td>
                    </tr>)
                }
                </tbody>
            </table>
        </div>
    )
}

export default Table
import React, { useState } from 'react'
import { formatDate } from '../../helpers/file'
import useFileOperations from '../../hooks/fileOperationsHook'
import { useStateValue } from '../../state'
import '../../styles/blocks/table.scss'
import ActionsPopUp from '../atoms/ActionsPopUp'

/*
    @props {array} data
    @props {String} searchText
*/
function Table(props) {
    const [ { dirs, selectedFileActions }, dispatch ] = useStateValue()
    const { goBack, navigateToDir } = useFileOperations()

    function areFilesEqual(f1, f2) {
        let areEqual = true
        if((f1 !== null && f2 !== null) && Object.keys(f1).length === Object.keys(f2).length) {
            for(let i = 0; i < Object.keys(f1).length; i++) {
                if(!(Object.keys(f1)[i] === Object.keys(f2)[i] 
                    && Object.values(f1)[i] === Object.values(f2)[i])) {
                        areEqual = false
                }
            }
        } else 
            areEqual = false

        return areEqual
    }

    function handleRowsMoreOptionsClick(e, file) {
        console.log(areFilesEqual(file, selectedFileActions))
        if(areFilesEqual(file, selectedFileActions)){
            dispatch({
                type: 'changeSelectedFileActions',
                value: null
            })
        } else{
            dispatch({
                type: 'changeSelectedFileActions',
                value: file
            })
        }

        e.stopPropagation()
    }

    function handleNavigateDirClick(file) {
        if(file.type !== "file") {
            dispatch({
                type: 'changeSelectedFileActions',
                value: null
            })
            navigateToDir(file.file_name)
        }
    }

    return (
        <div className="table-container">
            <div className="table-actions">
                <button onClick={goBack}><img src="./assets/icons/back_arrow_icon.svg" alt="" /></button>
                <p className="dark-text">{ dirs.join("/") + '/' }</p>
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
                { props.data && props.data.map(file =>
                    (props.searchText === '' || file.file_name.includes(props.searchText)) &&
                        <tr key={file.file_name}
                            onClick={() => handleNavigateDirClick(file)}>
                            <td><div className="file-type"></div></td>
                            <td className="dark-text">{ file.file_name }</td>
                            
                            <td className="light-text">{ formatDate(file.file_created_at) }</td>
                            <td className="light-text">{ file.file_size }</td>
                            <td>
                                <span className="more-btn" onClick={e => handleRowsMoreOptionsClick(e, file)}>
                                    <img src="./assets/icons/three_dot_icon.svg" alt="" />

                                    { areFilesEqual(selectedFileActions, file) &&
                                        <ActionsPopUp
                                            handlePopupDisplay={handleRowsMoreOptionsClick} /> }
                                </span>
                            </td>
                        </tr>) }
                </tbody>
            </table>
        </div>
    )
}

export default Table
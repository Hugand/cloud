import React, { useEffect, useState } from 'react'
import useFileOperations from '../../../hooks/fileOperationsHook'

function MoveFileBox(props) {
    const [ currDir, setCurrDir ] = useState(['.'])
    const [ foldersList, setFoldersList ] = useState([])
    const { getFoldersInDir } = useFileOperations()

    useEffect( () => {
        fetchData()
    }, [currDir])

    async function fetchData() {
        const foldersList = await getFoldersInDir(currDir)
        console.log(foldersList)
        if(foldersList !== null)
            setFoldersList(foldersList)
    }
    
    function goBack() {
        const dir = currDir
        if(dir.length > 1) {
            dir.pop()
            setCurrDir(dir)
            fetchData()
        }
    }

    function changeDir(newDir) {
        const dir = currDir
        dir.push(newDir)
        console.log(dir)
        setCurrDir(dir)
        fetchData()
    }
    
    return (
    <>
        <h1 className="modal-title">Move file</h1>
        
        <button onClick={goBack}>back</button>
        <div className="folder-list-container" style={{
            display:'flex',
            flexDirection: 'column'}}>
            { foldersList !== null && foldersList.map(f => 
                <div key={f} onClick={() => changeDir(f)}>{ f }</div>) }
        </div>

    </>)
}

export default MoveFileBox
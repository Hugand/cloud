import API from '../helpers/Api'
import { useStateValue } from '../state'

function useFileOperations() {
    const [ {dirs, socket, isConnected }, dispatch ] = useStateValue()

    const createDir = async newDirName => {
        const dirStack = [ ...dirs ]
        dirStack.push(newDirName)

        console.log(dirStack)

        const dirPathName = `./${dirStack.join('/')}`
        const res = await API.createDir(dirPathName)
        const toastData = {
            icon: 'error_icon.svg',
            msg: '',
            isVisible: true
        }

        switch(res.status) {
            case 'success':
                toastData.msg = "Success: Folder created!"
                toastData.icon = 'success_icon.svg'
                break
            case 'error':
                if(res.desc === 'FILE_ALREADY_EXISTS')
                    toastData.msg = `Error: Folder '${dirPathName}' already exists.`
                else
                    toastData.msg = `Error: Failed to create folder '${dirPathName}'.`
                break;
            default:
                toastData.msg = `Error: Failed to create folder '${dirPathName}'.`
        }

        dispatch({
            type: 'changeToast',
            value: toastData
        })
    }

    const navigateToDir = dirName => {
        let newDirStack = [ ...dirs, dirName ]
        const tmp = [...dirs]
        dispatch({
            type: 'changeDirs',
            value: newDirStack
        })
        API.changeDir(socket, isConnected, newDirStack, tmp)
    }

    const goBack = () => {
        let dirStack = [...dirs]
        const tmp = [...dirs]

        dirStack.pop()
        dispatch({
            type: 'changeDirs',
            value: dirStack
        })
        API.changeDir(socket, isConnected, dirStack, tmp)
    }

    const deleteFile = async (fileName) => {
        const fileDirToDelete = `${dirs.join('/')}/${fileName}`
        let res = await API.deleteFile(fileDirToDelete)
        const toastData = {
            isVisible: true,
            icon: 'error_icon.svg',
            msg: ''
        }

        switch(res.status) {
            case 'success':
                toastData.icon = 'success_icon.svg'
                toastData.msg = 'Success: File deleted!'
                break;
            case 'error':
                if(res.desc === 'PATH_INVALID'){
                    toastData.msg = `Error: The given path is invalid (${fileDirToDelete}`
                } else {
                    toastData.msg = `Error: Error deleting file (${fileDirToDelete}`
                }
                break;
            case 'failed':
            default:
                toastData.msg = `Error: Error deleting file (${fileDirToDelete}`
        }

        dispatch({
            type: 'changeToast',
            value: toastData
        })
    }

    const renameFile = async (prevName, newName) => {
        const fileDir = `${dirs.join('/')}/`
        const res = await API.renameFile(fileDir, prevName, newName)
        const toastData = {
            isVisible: true,
            icon: 'error_icon.svg',
            msg: ''
        }

        switch(res.status) {
            case 'success':
                toastData.icon = 'success_icon.svg'
                toastData.msg = 'Success: File renamed!'
                break;
            case 'error':
                switch(res.desc) {
                    case 'FILE_ALREADY_EXISTS':
                        toastData.msg = `Error: There's already a file named "${newName}"`
                        break
                    case 'FILE_DOESNT_EXIST':
                        toastData.msg = `Error: The file with the name "${prevName}" doesn't exist`
                        break
                    default:
                        toastData.msg = `Error: Error renaming file (${prevName}`
                }
                break;
            case 'failed':
            default:
                toastData.msg = `Error: Error renaming file (${prevName}`
        }
            
        dispatch({
            type: 'changeToast',
            value: toastData
        })
    }

    const moveFile = async (fileName, newDirStack) => {
        const currDir = `./${dirs.join('/')}/`
        const newDir = `${newDirStack.join('/')}/`
        const res = await API.moveFile(fileName, currDir, newDir)
        const toastData = {
            isVisible: true,
            icon: 'error_icon.svg',
            msg: ''
        }

        switch(res.status) {
            case 'success':
                toastData.icon = 'success_icon.svg'
                toastData.msg = `Success: File moved to "${newDir + fileName}"!`
                break;
            case 'error':
                switch(res.desc) {
                    case 'FILE_ALREADY_EXISTS':
                        toastData.msg = `Error: The file already exists ("${newDir + fileName}")`
                        break
                    case 'FILE_DOESNT_EXIST':
                        toastData.msg = `Error: No file with the name "${currDir + fileName}"`
                        break
                    default:
                        toastData.msg = `Error: Error moving file (${currDir + fileName}`
                }
                break;
            case 'failed':
            default:
                toastData.msg = `Error: Error moving file (${currDir + fileName}`
        }

        dispatch({
            type: 'changeToast',
            value: toastData
        })

        return res.status === 'success'
    }

    const getFoldersInDir = async (dirStack) => {
        const dirToList = dirStack.join('/') + '/'
        const res = await API.getFoldersInDir(dirToList)
        switch(res.status) {
            case 'success':
                return res.folderList
            case 'error':
            default:
                dispatch({
                    type: 'changeToast',
                    value: {
                        isVisible: true,
                        icon: 'error_icon.svg',
                        msg: "Error: Error retrieving folder's list"
                    }
                })
                return null
        }
    }

    const downloadFile = async filename => {
        const dirStack = [ ...dirs ]
        dirStack.push(filename)
        const res = await API.downloadFile(dirStack.join('/'))

        if(res.status === 200) {
            const blob = await res.blob()
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = filename;
            a.click();
        } else if(res.status === 422)
            dispatch({
                type: 'changeToast',
                value: {
                    msg: 'Error: Invalid file to download',
                    icon: 'error_icon.svg',
                    isVisible: true
                }
            })
        
    }

    return {
        navigateToDir,
        goBack,
        deleteFile,
        renameFile,
        moveFile,
        getFoldersInDir,
        createDir,
        downloadFile
    }
}

export default useFileOperations
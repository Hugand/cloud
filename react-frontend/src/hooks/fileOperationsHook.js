import API from '../helpers/Api'
import { useStateValue } from '../state'

function useFileOperations() {
    const [ {dirs, socket, isConnected }, dispatch ] = useStateValue()

    const navigateToDir = dirName => {
        let newDirStack = [
        ...dirs,
        dirName
        ]
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
        console.log(res)
        switch(res.status) {
            case 'success':
                dispatch({
                    type: 'changeToast',
                    value:{
                        icon: 'success_icon.svg',
                        isVisible: true,
                        msg: 'Success: File deleted!'
                    }
                })
                break;
            case 'error':
                if(res.desc === 'PATH_INVALID'){
                    dispatch({
                        type: 'changeToast',
                        value:{
                            icon: 'error_icon.svg',
                            isVisible: true,
                            msg: `Error: The given path is invalid (${fileDirToDelete}`
                        }
                    })
                } else {
                    dispatch({
                        type: 'changeToast',
                        value:{
                            icon: 'error_icon.svg',
                            isVisible: true,
                            msg: `Error: Error deleting file (${fileDirToDelete}`
                        }
                    })
                }
                break;
            case 'failed':
            default:
                dispatch({
                    type: 'changeToast',
                    value:{
                        icon: 'error_icon.svg',
                        isVisible: true,
                        msg: `Error: Error deleting file (${fileDirToDelete}`
                    }
                })
        }
        console.log(res)
    }

    const renameFile = (prevName, newName) => {
        const fileDir = `${dirs.join('/')}/`
        const res = API.renameFile(fileDir, prevName, newName)
    }

    return {
        navigateToDir,
        goBack,
        deleteFile,
        renameFile
    }
}

export default useFileOperations
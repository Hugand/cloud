
export const initialState = {
    dirs: [],
    socket: null,
    isConnected: false
}

/*
    action: { type, value }
*/
export const reducer = (state, action) => {
    switch(action.type) {
        case 'changeDirs':
            return {
                ...state,
                dirs: action.value
            }
        case 'changeSocket':
            return {
                ...state,
                socket: action.value
            }
        case 'changeIsConnected':
            return {
                ...state,
                isConnected: action.value
            }

        default:
            return state
    }
}
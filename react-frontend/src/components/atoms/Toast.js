import React from 'react'

/*
    @props {String} msg
*/
function Toast(props) {
    return (
        <div className="toast-container">
            <p>{ props.msg }</p>
        </div>
    )
}

export default Toast

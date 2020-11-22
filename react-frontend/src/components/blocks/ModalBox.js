import React from 'react'

/*
    @props {Component} component
*/
function ModalBox(props) {
    return (
        <div className="bg">
            <div className="modal-container">
                { props.component ? props.component : "" }
            </div>
        </div>
    )
}

export default ModalBox
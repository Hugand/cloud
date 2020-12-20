import React from 'react'
import '../../styles/blocks/modal-box.scss'

/*
    @props {Component} component
    @props {boolean} isDisplayed
    @props {function} handleModalToggle
*/
function ModalBox(props) {
    return props.isDisplayed ? (
        <div className="bg" onClick={() => props.handleModalToggle(false)}>
            <div className="modal-container" onClick={e => e.stopPropagation()}>
                <button className="btn-icon" onClick={() => props.handleModalToggle(false)}><img src='./assets/icons/cross_icon.svg' alt='close'/></button>
                { props.component ? props.component : "" }
            </div>
        </div>
    ) : <></>
}

export default ModalBox
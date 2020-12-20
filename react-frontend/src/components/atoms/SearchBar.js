import React from 'react'
import '../../styles/atoms/search-bar.scss'

/*
    @props {Function} setSearchText
*/
function SearchBar(props) {
    function handleSearchTextChange(e) {
        props.setSearchText(e.target.value)
    }

    return (
        <div className="search-bar-container">
            <img className="search-icon" src="./assets/icons/search_icon.svg" alt="" />
            <input type="text" onChange={handleSearchTextChange} placeholder="Search file"/>
        </div>
    )
}

export default SearchBar
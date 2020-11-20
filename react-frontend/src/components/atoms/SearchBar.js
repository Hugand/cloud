import React, { useState } from 'react'
import '../../styles/atoms/search-bar.scss'

function SearchBar(props) {
    const [ searchText, setSearchText ] = useState("");

    return (
        <div className="search-bar-container">
            <img className="search-icon" src="./assets/icons/search_icon.svg" alt="" />
            <input type="text" onChange={e => setSearchText(e.target.value)} placeholder="Search file"/>
        </div>
    )
}

export default SearchBar
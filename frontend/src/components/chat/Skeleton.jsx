import React from "react";
import '../style/Skeleton.css';

const Skeleton = () => {
    return (
        <div className='skeleton'>
            <div className='skeleton-avatar'/>
            <div className='skeleton-author'/>
            <div className='skeleton-description'/>
        </div>
    )
}

export default Skeleton
import React from 'react';
import './Popup.css';

interface PopupProps {
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}

const Popup: React.FC<PopupProps> = ({ message, onConfirm, onCancel }) => {
  return (
    <div className="popup-overlay">
      <div className="popup-content">
        <p>{message}</p>
        <div className="popup-buttons">
          <button onClick={onConfirm}>확인</button>
          <button onClick={onCancel}>취소</button>
        </div>
      </div>
    </div>
  );
};

export default Popup;
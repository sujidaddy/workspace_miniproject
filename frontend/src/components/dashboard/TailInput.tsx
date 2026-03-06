
// import { type Ref } from "react"

// interface TailInputProps {
//   type : string, 
//   name : string, 
//   id : string,
//   placeholder? : string,
//   ref :Ref<HTMLInputElement>
// }

// export default function TailInput({type, name, id, placeholder, ref}: TailInputProps) {
//   return (
//     <div className='w-full'>
//       <input  type={type} 
//               name={name} 
//               id= {id} 
//               placeholder={placeholder}
//               ref={ref}
//               className="w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500" />
//     </div>
//   )
// }아래 gpt수정코드 위 원본
import { forwardRef, InputHTMLAttributes } from 'react';

interface TailInputProps
  extends InputHTMLAttributes<HTMLInputElement> {}

const TailInput = forwardRef<HTMLInputElement, TailInputProps>(
  ({ className, ...props }, ref) => {
    return (
      <div className="w-full">
        <input
          ref={ref}
          {...props}
          className={`w-full p-2 text-gray-900 border border-gray-300 rounded-lg bg-gray-50 text-base focus:ring-blue-500 focus:border-blue-500 ${className ?? ''}`}
        />
      </div>
    );
  }
);

TailInput.displayName = 'TailInput';

export default TailInput;
